using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

public class Snake : MonoBehaviour {

	public GameObject tailPrefab;
	public float speed = 1.3f;
	Vector3 direction = Vector3.right * 1.3f;
	List<Transform> tail = new List<Transform>();
	bool ate = false;


	void Start () 
	{
		InvokeRepeating ("Move", 0.2f, 0.2f);
	}


	void Update () 
	{
		if (Input.GetKey (KeyCode.W))
			direction = Vector3.up * speed;
		else if (Input.GetKey (KeyCode.S))
			direction = Vector3.down * speed;
		else if (Input.GetKey (KeyCode.A))
			direction = Vector3.left * speed;
		else if (Input.GetKey (KeyCode.D))
			direction = Vector3.right * speed;
	}


	void Move()
	{
		// Save current position (gap will be here)
		Vector3 currPosition = transform.position;

		// Move head into new direction (now there is a gap)
		transform.Translate (direction);

		// If snake ate something, insert new Element into gap
		if (ate) 
		{
			//load prefab into world
			GameObject newBlock = (GameObject)Instantiate(tailPrefab, currPosition, Quaternion.identity);

			//Keep track of it in our tail list
			tail.Insert (0, newBlock.transform);

			//Reset the flag
			ate = false;
		}

		if (tail.Count > 0) 
		{
			// Move last tail ELement to where the head was
			tail.Last().position = currPosition;

			// Add to front of list, remove from the back
			tail.Insert(0, tail.Last ());
			tail.RemoveAt (tail.Count-1);
		}


	}


	void OnTriggerEnter2D(Collider2D collider)
	{
		if (collider.name.StartsWith ("FoodPrefab")) 
		{
			ate = true;

			Destroy (collider.gameObject);
		} 

		else 
		{

		}
	}



}
