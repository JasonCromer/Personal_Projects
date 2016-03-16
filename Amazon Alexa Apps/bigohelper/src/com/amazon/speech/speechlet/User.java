/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.amazon.speech.speechlet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The {@code User} tied to a {@code Speechlet} {@code Session} is the user registered to the device
 * initiating the {@code Speechlet} {@code Session} and contains a unique identifier.
 *
 * @see Speechlet
 * @see Session
 * @see SpeechletException
 */
public class User {
    private final String userId;
    private final String accessToken;

    /**
     * Returns a new builder instance used to construct a new {@code IntentRequest}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Constructs a user.
     *
     * @param userId
     *            the user identifier
     * @deprecated use {@link User#builder()} to construct new instances of this class
     */
    @Deprecated
    public User(final String userId) {
        this(userId, null);
    }

    @SuppressWarnings("unused")
    private User() {
        this(null, null);
    }

    /**
     * Private constructor to return a new {@code User} from a {@code Builder}.
     *
     * @param builder
     *            the builder used to construct the {@code User}
     */
    private User(final Builder builder) {
        this.userId = builder.userId;
        this.accessToken = builder.accessToken;
    }

    /**
     * Private constructor used for JSON serialization.
     *
     * @param userId
     *            the user identifier
     * @param accessToken
     *            the access token
     */
    private User(@JsonProperty("userId") final String userId,
            @JsonProperty("accessToken") final String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
    }

    /**
     * Returns the user identifier.
     *
     * @return the user identifier
     */
    @JsonInclude(Include.NON_EMPTY)
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the access token.
     *
     * @return the access token. Returns null if the user has not yet linked their account, or if
     * the skill is not configured for account linking.
     */
    @JsonInclude(Include.NON_EMPTY)
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Builder used to construct a new {@code User}.
     */
    public static final class Builder {
        private String userId;
        private String accessToken;

        private Builder() {
        }

        public Builder withUserId(final String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withAccessToken(final String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
